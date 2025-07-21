#odyshbayeh-1201462
import csv
import numpy as np

allocation_file_path = r'C:\Users\MSI\Desktop\OS_PROJECT#3\Allocation.csv'
request_file_path = r'C:\Users\MSI\Desktop\OS_PROJECT#3\Request.csv'
available_file_path = r'C:\Users\MSI\Desktop\OS_PROJECT#3\Available.csv'

# Read Allocation.csv
with open(allocation_file_path, 'r') as file:
    allocation_data = list(csv.DictReader(file))

# Read Request.csv
with open(request_file_path, 'r') as file:
    request_data = list(csv.DictReader(file))

# Read Available.csv
with open(available_file_path, 'r') as file:
    available_data = list(csv.DictReader(file))

# Extract process names and resource names
process_names = [row['Process'] for row in allocation_data]
resource_names = list(allocation_data[0].keys())[1:]

# Initialize NumPy arrays for allocation, request, and available
allocation = np.array([[int(row[resource]) for resource in resource_names] for row in allocation_data])
request = np.array([[int(row[resource]) for resource in resource_names] for row in request_data])
available = np.array([int(available_data[0][resource]) for resource in resource_names])

# Check if dimensions are consistent
if allocation.shape[0] != request.shape[0] or allocation.shape[1] != request.shape[1]:
    print("Error: Dimensions of allocation and request matrices are not consistent")
    exit()
else:
    print("Dimensions of allocation and request matrices are consistent\n")


# Check if dimensions match the number of resources in the available vector
if allocation.shape[1] != available.shape[0]:
    print("Error: Dimensions of allocation and available matrices are not consistent")
    exit()
else:
    print("Dimensions of allocation and available matrices are consistent\n")

# Check for a safe sequence using Banker's algorithm
def is_safe_sequence(available, allocation, request):
    work = available.copy()
    finish = np.zeros(request.shape[0], dtype=bool)

    while True:
        found = False
        for i in range(request.shape[0]):
            if not finish[i] and all(request[i, :] <= work):
                finish[i] = True
                work += allocation[i, :]
                found = True

        if not found:
            break

    return all(finish)

# Define a function to check for deadlocks and resolve them
def resolve_deadlock(available, allocation, request, process_names):
    max_attempts = 10  # Max increments to try
    resource_increment = 1  # Increment each resource by 1

    for attempt in range(max_attempts):
        if is_safe_sequence(available, allocation, request):
            # Find and print a new safe sequence
            print(f"Deadlock resolved after {attempt} increments.")
            work = available.copy()
            finish = np.zeros(request.shape[0], dtype=bool)
            safe_sequence = []

            while not all(finish):
                for i in range(request.shape[0]):
                    if not finish[i] and all(request[i, :] <= work):
                        finish[i] = True
                        safe_sequence.append(process_names[i])
                        work += allocation[i, :]
            
            print("New execution sequence without deadlock:")
            for process in safe_sequence:
                print(f"Executing process {process}")
            return  # Exit the function once a safe sequence is found
        
        # Increment available resources
        available += np.full(available.shape, resource_increment)

    print("Unable to resolve deadlock after maximum attempts.")

# Check if there is a safe sequence
if is_safe_sequence(available, allocation, request):
    print("No deadlock, there is a safe sequence.\nExecution sequence without deadlock:")

    # Find and print a series of process executions that are possible without deadlock
    work = available.copy()
    finish = np.zeros(request.shape[0], dtype=bool)
    safe_sequence = []

    while not all(finish):
        processes_executed = False
        for i in range(request.shape[0]):
            if not finish[i] and all(request[i, :] <= work):
                finish[i] = True
                safe_sequence.append(process_names[i])
                work += allocation[i, :]
                processes_executed = True

        # Check if all processes are blocked (no processes can be executed)
        if not processes_executed:
            break

    for process in safe_sequence:
        print(f"Executing process {process}")

else:
    print("Deadlock detected, there is no safe sequence.")

    # Find and print the deadlocked processes
    work = available.copy()
    finish = np.zeros(request.shape[0], dtype=bool)
    deadlocked_processes = []

    for _ in range(request.shape[0]):
        found = False
        for i in range(request.shape[0]):
            if not finish[i] and all(request[i, :] <= work):
                finish[i] = True
                work += allocation[i, :]
                found = True

        if not found:
            deadlocked_processes.append(process_names[i])

    # Remove duplicates from the list of deadlocked processes
    deadlocked_processes = list(set(deadlocked_processes))

    print(f"Deadlocked processes: {', '.join(deadlocked_processes)}")

    # Attempt to resolve deadlock if we encountered one 
    resolve_deadlock(available, allocation, request, process_names)
