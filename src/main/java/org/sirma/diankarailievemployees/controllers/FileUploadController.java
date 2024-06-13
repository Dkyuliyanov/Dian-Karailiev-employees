package org.sirma.diankarailievemployees.controllers;

import lombok.RequiredArgsConstructor;
import org.sirma.diankarailievemployees.model.WorkEntry;
import org.sirma.diankarailievemployees.services.CsvReaderService;
import org.sirma.diankarailievemployees.model.EmployeePair;
import org.sirma.diankarailievemployees.services.EmployeePairServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Controller
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final CsvReaderService csvReaderService;
    private final EmployeePairServiceImpl employeePairServiceImpl;

    @PostMapping
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a non-empty file to upload.");
            return new ModelAndView("redirect:/errorPage");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            boolean isReady = reader.ready();
            if (!isReady) {
                redirectAttributes.addFlashAttribute("errorMessage", "The file is somehow unreadable.");
                return new ModelAndView("redirect:/errorPage");
            }

            try (Scanner scanner = new Scanner(file.getInputStream())) {
                boolean isEmpty = true;

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (!line.trim().isEmpty()) {
                        isEmpty = false;
                        break;
                    }
                }

                if (isEmpty) {
                    redirectAttributes.addFlashAttribute("errorMessage", "The file is empty or contains only whitespace.");
                    return new ModelAndView("redirect:/errorPage");
                }
            }

            List<WorkEntry> allWorkEntries = csvReaderService.readAllWorkEntries(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            if (allWorkEntries.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "No valid work entries found in the file.");
                return new ModelAndView("redirect:/errorPage");
            }

            List<EmployeePair> allPairs = employeePairServiceImpl.findAllWorkingPairs(allWorkEntries);
            if (allPairs.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "No overlapping employee pairs found.");
                return new ModelAndView("redirect:/errorPage");
            }

            ModelAndView modelAndView = new ModelAndView("results");
            modelAndView.addObject("employeePairs", allPairs);
            return modelAndView;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to process file: " + e.getMessage());
            return new ModelAndView("redirect:/errorPage");
        }
    }
}
