#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void flip(int * list, int n) {
	int i, tmp;
	for (i=0; i<n/2; i++) {
		tmp = list[i];
		list[i] = list[n-i-1];
		list[n-i-1] = tmp;
	}
}

void dosort(int *, int);
int main(void) {
	int i, n;
	int list[5000];
	char str[5000];
	char * tmpstr;
	FILE * f = fopen("test.txt", "r");

	if (!f) {
		printf("failed to open test.txt\n");
		return 1;
	}

	while (fgets(str, 5000, f)) {
		n = 0;
		memset(list, 0, 5000);
		tmpstr = strtok(str, " ");
		while (tmpstr) {
			list[n++] = atoi(tmpstr);
			tmpstr = strtok(NULL, " ");
		}
		dosort(list, n);
		printf("0\n");
	}
}

void dosort(int * list, int n) {
	int i, cnt=0;
	int sort[5000] = {0};
	int * sort_cur = sort;

	for (int current=n-1; current>=0; current--) {
		int max_idx = 0;
		for (i=0; i<=current; i++)
			if (list[i] > list[max_idx])
				max_idx = i;
		
		if (current != max_idx) {
			if (max_idx != 0) {
				flip(list, max_idx+1);
				cnt++;
				*(sort_cur++) = n-max_idx;
			}
			flip(list, current+1);
			cnt++;
			*(sort_cur++) = n-current;
		}
	}
	for (sort_cur = sort; *sort_cur; sort_cur++)
		printf("%d ", *sort_cur);
}
