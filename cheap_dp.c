#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct FINAL FINAL;
struct FINAL {
    int record[100];
    int sum;
    int record_idx;
};
int mod(int n, int d) {
    while (n<0)
        n += d;
    while (n>=d)
        n -= d;
    return n;
}

int m,n;

int main(void) {
    FILE * f = fopen("input.txt", "r");
    if (!f) {
        printf("file not found");
        return 1;
    }

    int i,j;
    int m,n;
    int path[10][100];
    FINAL dp[10][100];

    while (fscanf(f, "%d %d", &m, &n) != EOF) {
        memset(path, 0, n*m);
        memset(dp, 0, n*m);

        for (i=0; i<m; i++)
            for (j=0; j<n; j++)
                fscanf(f, "%d", &(path[i][j]));

        // 1. first col 
        for (i=0; i<m; i++) {
            FINAL record;
            record.sum = path[i][n-1];
            record.record_idx = n-1;
            record.record[record.record_idx--] = path[i][n-1];
            dp[i][n-1] = record;
        }

        // 2. cols
        for (j=n-2; j>=0; j--) // j col i row
            for (i=0; i<m; i++) {
                FINAL record_sb = dp[mod(i-1, m)][j+1];
                FINAL record_eq = dp[i][j+1];
                FINAL record_add = dp[mod(i+1, m)][j+1];

                FINAL * record_min = &record_sb;
                if (record_eq.sum < record_min->sum)
                    record_min = &record_eq;
                if (record_add.sum < record_min->sum)
                    record_min = &record_add;

                dp[i][j] = *record_min;
                dp[i][j].sum += path[i][j];
                dp[i][j].record[dp[i][j].record_idx--] = path[i][j];
            }

        // 3. print out result
        FINAL * result = &(dp[0][0]);
        for (i=1; i<m; i++) {
            if (dp[i][0].sum < result->sum)
                result = &(dp[i][0]);
        }
        for (i=0; i<n; i++) {
            printf("%d ", result->record[i]);
        }
        printf("\n%d\n", result->sum);
    }
}