#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int dothing(char *, char *);
int main(void) {
	FILE * f = fopen("input.txt", "r");
	if (!f) {
		printf("input.txt not found");
		return 1;
	}

	char str1[10000];
	char str2[100];
	int i,j,k;
	int n;

	fscanf(f, "%d", &n);
	for (i=0; i<n; i++) {
		memset(str1, 0, 10000);
		memset(str2, 0, 100);
		fscanf(f, "%s", str1);
		fscanf(f, "%s", str2);
		printf("%d\n", dothing(str1, str2));
	}
}

int dothing(char *str1, char *str2) {
	int cnt = 0;
	char *cur1 = str1;
	char *cur2 = str2;

	while (*cur1) {
		if (*cur1 == *cur2) {
			if (*(cur2+1))
				cnt += dothing(cur1+1, cur2+1); 
			else cnt++;
		}
		cur1++;
	}
	return cnt;
}
