#include <stdio.h>
#include <stdlib.h>

int main(void) {
	FILE * f = fopen("test.txt", "r");
	if (!f) {
		printf("file not found");
		return 1;
	}

	int i,n;
	int *list;

	while (1) {
		fscanf(f, "%d", &n);
		if (!n) break;
		list = (int *) malloc(sizeof(int) * n);

		int avg = 0;
		for (i=0; i<n; i++) {
			fscanf(f, "%d", list+i);
			avg += *(list+i);
		}
		avg /= n;

		int total = 0;
		for (i=0; i<n; i++) {
			int minus = list[i] - avg;
			if (minus > 0)
				total += minus;
		}

		printf("%d\n", total);
		free(list);
	}
}
