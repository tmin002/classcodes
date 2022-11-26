#include <stdio.h>
#include <stdbool.h>
#define N 29
#define NOT_COLORED 0
#define RED 1
#define BLACK 2

typedef struct RECORD RECORD;
struct RECORD {
    // traverse_record values
    // 0: no need to visit. not connected.
    // 1: way not visited yet.
    // 2: way visited before.
    int traverse_record[N][N];

    // color_record values
    // define..
    int color_record[N];
    int current_color;
    int current_node;
    int previous_node;
};

int i,j,k,l; // counters
int dot_count, line_count;
int d[N][N] = {0};

RECORD new_record(RECORD prev_record, int new_start) {
    RECORD new;
    new.current_color = prev_record.current_color == BLACK ? RED : BLACK;

    for (i=0; i<N; i++) {
        for (j=0; j<N; j++)
            new.traverse_record[i][j] = prev_record.traverse_record[i][j];
        new.color_record[i] = prev_record.color_record[i];
    }
    new.color_record[new_start] = new.current_color;
    new.current_node = new_start;
    new.previous_node = prev_record.current_node;

    return new;
}

bool backtrack(RECORD record) {
    bool leaf = true;
    bool possible = true;

    for (int i=0; i<dot_count && possible; i++) {
        if (i == record.current_node)
            // skip two same nodes
            continue;

        if (d[i][record.current_node] && i != record.previous_node) {
            // connected to something else then previous node
            leaf = false;

            // check adjacent node's color is same as mine
            if (record.color_record[i] == record.current_color) {
               return false;
            }

            // make new traverse record and recurse again
            else if (record.color_record[i] == NOT_COLORED) {
                possible = backtrack(new_record(record, i));
            }

            // if d[i][start] != start_color, possible will remain true.
        }
    }

    return leaf || possible;
}
int main() {

    // initialize
    FILE * f = fopen("input.txt", "r");
    if (!f) {
        printf("file not found\n");
        return 1;
    }

    // get input
    fscanf(f, "%d", &dot_count);
    fscanf(f, "%d", &line_count);

    int dot1, dot2;
    for (i=0; i<line_count; i++) {
        fscanf(f, "%d %d", &dot1, &dot2);
        d[dot1][dot2] = 1;
        d[dot2][dot1] = 1;
    }

    // do back tracking with mst and print out result
    RECORD root_record;
    root_record.current_color = BLACK;
    root_record.current_node = dot1;
    root_record.previous_node = -1;
    for (i=0; i<N; i++) {
        for (j=0; j<N; j++) {
            root_record.traverse_record[i][j] = 0;
        }
        root_record.color_record[i] = NOT_COLORED;
    }
    root_record.color_record[dot1] = BLACK;

    if (!backtrack(root_record)) {
        printf("not ");
    }
    printf("two-color\n");
    return 0;
}
