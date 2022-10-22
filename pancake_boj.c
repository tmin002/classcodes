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
	int i, j, n, cnt;
	int list[5000];
	scanf("%d", &n);

	for (i=0; i<n; i++) {
		int cnt;
		scanf("%d", &cnt);
		memset(list, 0, 5000);
		for (j=0; j<cnt; j++)
			scanf("%d", list+j);
		dosort(list, cnt);
		printf("\n");
	}
}

void dosort(int * list, int n) {
	int i, j, cnt=0;
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
				*(sort_cur++) = max_idx+1;
			}
			flip(list, current+1);
			cnt++;
			*(sort_cur++) = current+1;
		}
	}
	printf("%d ", cnt);
	for (sort_cur = sort; *sort_cur; sort_cur++)
		printf("%d ", *sort_cur);
}
