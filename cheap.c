#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int path[10][100]; 
int m,n;

typedef struct FINAL FINAL;
struct FINAL {
    int * record;
    int sum;
};

int * listclone(int * list, int size) {
    int * result = (int *) malloc(sizeof(int) * size);
    for (int i=0; i<size; i++)
        result[i] = list[i];
    return result;
}
int * listcat(int * list, int size, int add) {
    int * result = listclone(list, size+1);
    result[size] = add;
    return result;
}
FINAL dothing(int row, int col, int * record, int sum) {
    while (row < 0)
        row += m;
    while (row >= m)
        row -= m;

    record = listcat(record, col, path[row][col]);
    sum += path[row][col];

    if (col == n-1) {
        FINAL f = {record, sum};
        return f;
    } else {
        FINAL total_sb = dothing(row-1, col+1, listclone(record, col+1), sum);
        FINAL total_eq = dothing(row, col+1, listclone(record, col+1), sum);
        FINAL total_ad = dothing(row+1, col+1, listclone(record, col+1), sum);

        FINAL * mintot = &total_sb;
        if (total_eq.sum < mintot->sum) mintot = &total_eq;
        if (total_ad.sum < mintot->sum) mintot = &total_ad;
        return *mintot;
    }
}

int main(void) {
    FILE * f = fopen("input.txt", "r");
    if (!f) {
        printf("file not found");
        return 1;
    }
    while (fscanf(f, "%d %d", &m, &n) != EOF) {
        for (int i=0; i<m; i++)
        for (int j=0; j<n; j++)
            fscanf(f, "%d", path[i]+j);

        int init[1] = {};
        FINAL s = dothing(0, 0, init, 0);

        for (int i=0; i<n; i++) {
            printf("%d ", s.record[i]);
        }
        printf("\n%d\n", s.sum);
    }
}