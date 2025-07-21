//odyshbayeh-1201462
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <sys/wait.h>
#include <unistd.h>
#include <pthread.h>

typedef struct threadstructure{
    int matrix_id[100][100];
    int matrix_q[100][100];
    int result[100][100];
    int sr;
    int er;
} threadstructure;

void normal_multiplication(int result[100][100], int matrix_id[100][100], int matrix_q[100][100]) {
    for (int i = 0; i < 100; ++i) {
        for (int j = 0; j < 100; ++j) {
                result[i][j] += matrix_id[i][j] * matrix_q[i][j];
        }
    }
}
//odyshbayeh-1201462
void* worker_thread(void* arg) {
    threadstructure* data = (threadstructure*)arg;

    for (int i = data->sr; i < data->er; ++i) {
        for (int j = 0; j < 100; ++j) {
            data->result[i][j] = 0;
            for (int k = 0; k < 100; ++k) {
                data->result[i][j] += data->matrix_id[i][j] * data->matrix_q[i][j];
            }
        }
    }
}
//1201462
void matrix_multiplication_process(int process_count, int matrix_id[100][100], int matrix_q[100][100], int result[100][100]) {

    pid_t p_num[process_count];

    for (int i = 0; i < process_count; ++i) {
        int first_line = i * (100 / process_count);
        int last_line = (i + 1) * (100 / process_count);

        pid_t child_pid = fork();

        if (child_pid == 0) {
            for (int row = first_line; row < last_line; ++row) {
                for (int col = 0; col < 100; ++col) {
                    result[row][col] = 0;
                    for (int k = 0; k < 100; ++k) {
                        result[row][col] += matrix_id[row][k] * matrix_q[k][col];
                    }
                }
            }
            exit(1);
        }
    }
    for (int i = 0; i < process_count; ++i) {
        int status;
        waitpid(p_num[i], &status, 0);
    }
}
//odyshbayeh-1201462
void matrix_multiplication_threads(int thread_count, int matrix_id[100][100], int matrix_q[100][100], int result[100][100], int detached) {
    pthread_t t[thread_count];
    pthread_attr_t attr;

    if (detached) {
        pthread_attr_init(&attr);
        pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);
    }

    threadstructure thread_data[thread_count];

    for (int i = 0; i < thread_count; ++i) {
        thread_data[i].sr = i * (100 / thread_count);
        thread_data[i].er = (i + 1) * (100 / thread_count);
        for (int j = 0; j < 100; ++j) {
            for (int k = 0; k < 100; ++k) {
                thread_data[i].matrix_id[j][k] = matrix_id[j][k];
                thread_data[i].matrix_q[j][k] = matrix_q[j][k];
            }
        }
    }
    for (int i = 0; i < thread_count; ++i) {
        if (detached) {
            if (pthread_create(&t[i], &attr, worker_thread, &thread_data[i]) != 0) {
                fprintf(stderr, "pthread_create failed\n");
                exit(0);
            }
        } else {
            if (pthread_create(&t[i], NULL, worker_thread, &thread_data[i]) != 0) {
                fprintf(stderr, "pthread_create failed\n");
                exit(0);
            }
        }
    }
    if (detached) {
        usleep(10000);
    }
    for (int i = 0; i < thread_count; ++i) {
        for (int j = thread_data[i].sr; j < thread_data[i].er; ++j) {
            for (int k = 0; k < 100; ++k) {
                result[j][k] = thread_data[i].result[j][k];
            }
        }
    }
}

double timediff(struct timeval start, struct timeval end) {
    return (double)((end.tv_sec - start.tv_sec) + (end.tv_usec - start.tv_usec) / 1000.0);
}
int main() {

    int matrix_id[100][100];
    int matrix_q[100][100];
    int result[100][100];

    int option;

    int stdnum [7] = {1,2,0,1,4,6,2};
    int stdnumbyear [10] = {2,4,0,5,3,2,6,9,2,4};
    int counter1 = 0;
    int counter2 = 0;

    for (int i=0; i<100;i++)
    {
      for(int j=0; j<100;j++)
      {
      matrix_id[i][j]=stdnum[counter1];
      if (counter1 == 7)
      {
      counter1=0;
      }else {
          counter1++;
      }
      }
    }

    for (int i=0; i<100;i++)
    {
      for(int j=0; j<100;j++)
      {
      matrix_q[i][j]=stdnumbyear[counter2];
      if (counter2 == 10)
      {
      counter2=0;
      }else{
          counter2++;
      }
      }
    }

    struct timeval start, end;

    do {
        printf("odyshbayeh-1201462\n\n");
        printf("1 normal matrix multiplication\n");
        printf("2 matrix multiplication using threads\n");
        printf("3 matrix multiplication using fork\n");
        printf("4 matrix multiplication using detached threads\n");
        printf("5 run all four methods\n");
        printf("0 exit\n");
        printf("please enter a number from the options above : ");

        scanf("%d", &option);


        switch (option) {
            case 1:
                gettimeofday(&start, NULL);
                normal_multiplication(result, matrix_id, matrix_q);
                gettimeofday(&end, NULL);
                printf("the prosses took %lf milliseconds using normal multiplication\n", timediff(start, end));
                printf("\n\n");
                break;

            case 2:
                for (int thread_count = 2; thread_count <= 8; thread_count *= 2) {
                    gettimeofday(&start, NULL);
                    matrix_multiplication_threads(thread_count, matrix_id, matrix_q, result, 0);
                    gettimeofday(&end, NULL);
                    printf("number of threads used for the multiplication : %d  ",thread_count);
                    printf("the prosses took %lf milliseconds\n", timediff(start, end));

                }
                 printf("\n\n");
                break;

            case 3:
                for (int process_count = 2; process_count <= 8; process_count *= 2) {
                    gettimeofday(&start, NULL);
                    matrix_multiplication_process(process_count, matrix_id, matrix_q, result);
                    gettimeofday(&end, NULL);
                    printf("number of processes used for the multiplication : %d  ",process_count);
                    printf("the prosses took %lf milliseconds\n", timediff(start, end));

                }
                 printf("\n\n");
                break;
                case 4:
                for (int thread_count = 2; thread_count <= 8; thread_count *= 2) {
                    gettimeofday(&start, NULL);
                    matrix_multiplication_threads(thread_count, matrix_id, matrix_q, result, 1);
                    gettimeofday(&end, NULL);
                    printf("number of detached threads used for the multiplication : %d  ",thread_count);
                    printf("the prosses took %lf milliseconds\n", timediff(start, end));
                }
                printf("\n\n");
                break;

            case 5:
                gettimeofday(&start, NULL);
                normal_multiplication(result, matrix_id, matrix_q);
                gettimeofday(&end, NULL);
                printf("the prosses took %lf milliseconds using normal multiplication\n", timediff(start, end));

                for (int thread_count = 2; thread_count <= 8; thread_count *= 2) {
                    gettimeofday(&start, NULL);
                    matrix_multiplication_threads(thread_count, matrix_id, matrix_q, result, 0);
                    gettimeofday(&end, NULL);
                    printf("number of threads used for the multiplication : %d  ",thread_count);
                    printf("the prosses took %lf milliseconds\n", timediff(start, end));
                }

                for (int process_count = 2; process_count <= 8; process_count *= 2) {
                    gettimeofday(&start, NULL);
                    matrix_multiplication_process(process_count, matrix_id, matrix_q, result);
                    gettimeofday(&end, NULL);
                    printf("number of process used for the multiplication : %d  ",process_count);
                    printf("the prosses took %lf milliseconds\n", timediff(start, end));
                }
                for (int thread_count = 2; thread_count <= 8; thread_count *= 2) {
                    gettimeofday(&start, NULL);
                    matrix_multiplication_threads(thread_count, matrix_id, matrix_q, result, 1);
                    gettimeofday(&end, NULL);
                    printf("number of detached threads used for the multiplication : %d  ",thread_count);
                    printf("the prosses took %lf milliseconds\n", timediff(start, end));
                }
                printf("\n\n");
                break;
            case 0:
            option = 0;
                break;
        }
    } while (option != 0);

    return 0;
}
