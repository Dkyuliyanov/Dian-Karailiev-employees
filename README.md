
**GraalVM NATIVE for Ubuntu 22.04:** 

1. Build with:

    ```bash
    docker build -f Dockerfile -o . .
    ```
2. Run with:

   a. Make sure the result is executable: 
    ```bash
    chmod +x Dian-Karailiev-employees
    ```
   b. Run the executable:
    ```bash
    ./Dian-Karailiev-employees
    ```

**CLASSIC Docker**:

1. Build with:

    ```bash
    docker build -f Dockerfile.classic -t dian-karailiev-employees .
    ```

2. Run with:

    ```bash
    docker run -p 8080:8080 dian-karailiev-employees
    ```
