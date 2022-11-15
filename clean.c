#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <float.h>

typedef struct POINT POINT;
struct POINT {
    float x,y;
};

float calc_angle(POINT * p1, POINT * p2) {
    float angle;

    // We assume that two points cannot be the same.
    if (p1->y == p2->y) {
        angle = p1->x > p2->x ? M_PI : 0;
    } else if (p1->x == p2->x) {
        angle = p1->y > p2->y ? M_PI*1.5 : M_PI*0.5;
    } else {
        angle = (float) tanh((p1->y - p2->y) / (p1->x - p2->x));
        if (p1->x < p2->x) {
            if (p1->y > p2->y)
                angle += M_PI * 2;
        } else {
                angle += M_PI;
        }
    }

    return angle;
}

float eucdist(POINT * p1, POINT * p2) {
    return sqrt(pow(p1->x - p2->x, 2) + pow(p1->y - p2->y, 2));
}

int main(void) {

    int n, loop_n;
    int i, j, k; // counters

    // Init file descriptor
    FILE * f = fopen("input.txt", "r");
    if (!f) {
        printf("File not found\n");
        return 1;
    }

    // Read file
    fscanf(f, "%d", &n);
    for (; n>0; n--) {

        // POINT array
        POINT * lp;

        fscanf(f, "%d", &loop_n);
        lp = (POINT *) malloc(sizeof(POINT) * loop_n);

        // Put data into array and get lowest point
        POINT dummy_downmost = {0, FLT_MAX};
        POINT * downmost = &dummy_downmost;
        float x,y;

        for (i=0; i<loop_n; i++) {
            fscanf(f, "%f %f", &x, &y);
            lp[i].x = x;
            lp[i].y = y;
            if (y < downmost->y)
                downmost = lp+i;
        }

        // traverse every points and make hull
        POINT * prev_point = downmost;
        POINT * current_point;
        float prev_angle = 0;
        float hull_length = 0;

        float angle;
        float min_angle;
        while (current_point != downmost) {
            min_angle = FLT_MAX; 
            for (i=0; i<loop_n; i++) {
                if (lp+i == prev_point)
                    continue;

                angle = calc_angle(prev_point, lp+i);
                if (angle >= prev_angle && angle < min_angle) {
                    current_point = lp+i;
                    min_angle = angle;
                } else if (min_angle != 0 && angle == 0 && M_PI*2 < min_angle) {
                    current_point = lp+i;
                    min_angle = M_PI*2;
                }
            }
            if (min_angle == M_PI * 2) {
                printf("error: not found\n");
            }
            prev_angle = min_angle;
            hull_length += eucdist(prev_point, current_point);
            prev_point = current_point;
        }

        // one extra meters at each sides
        hull_length += 2;

        // print out result
        printf("%.2f\n", hull_length);
    }

    // close file
    fclose(f);
}
