
**NATIVE for Ubuntu 22.04:** 

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

**Usage**:

1. Open in browser:

    ```bash
    http://localhost:8080
    ```
2. Upload a CSV file, e.g.:

EmpID,ProjectID,DateFrom,DateTo
143,12,2013-11-01,2014-01-05
143,13,2009-01-01,2024-04-27
220,13,2010-09-15,2012-04-30
218,14,2012-05-16,NULL
218,15,2015-03-20,2016-07-25
219,12,2014-06-10,2015-08-20
221,14,2008-12-01,2010-01-15
222,15,2017-01-10,2019-03-22
223,12,2013-11-11,2014-12-31
224,10,2011-05-25,2013-06-01
225,11,2007-02-20,2010-10-30
226,10,2005-03-15,2007-11-22

