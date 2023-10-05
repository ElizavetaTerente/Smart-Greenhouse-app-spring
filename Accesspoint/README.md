# Accesspoint Setup

This document describes how to set up and run the Accesspoint application.

## Prerequisites

- Python 3.x
- pip (Python package installer)
- Bash shell (for running the setup script)

## Setup

1. Clone the repository or download the source code.

2. Navigate to the Accesspoint directory.

3. Run the setup script:

    ```bash
    ./configure
    ```

    The script will do the following:

    - Install the Python requirements specified in the `requirements.txt` file.
    - Check if the application is already configured to run at startup. If not, it will ask if you want to configure it to do so.
    - Check if a configuration file already exists. If it does, it will ask if you want to overwrite it.
    - If the configuration file does not exist or you choose to overwrite it, it will prompt you to enter the required data:
        - The address of the Webserver
        - A name for the building or room where the Accesspoint is located
        - Your username
        - Your password
        - The Accesspoint ID (optional)
    - Start the Python application.

## Running the Application

After the setup, the application will start automatically. If you've configured it to run at startup, it will also start every time the system boots.

To run the application manually, you can use the following command:

```bash
python3 main.py
