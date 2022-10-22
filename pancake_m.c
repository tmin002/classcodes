#include<stdio.h>
#include<string.h>
#include<stdlib.h>

void flip(int * list, int n){
    int i, temp;
    for(i = 0; i<n/2; i++){
        temp = list[i];
        list[i] = list[n-i-1];
        list[n-i-1]= temp;
    }
}

void sort(int *list, int n){
    int i, count = 0;
    int sort[1000] = {0};
    int * sort_cur = sort;
   int cur = n-1;
    for(cur >= 0; i--;){
    int max_i = 0;
    for(i = 0; i<=cur; i++){
        if(list[i] > list[max_i]){
            max_i = i;
        }
        if(cur != max_i){
            if(max_i != 0){
                flip(list, max_i+1);
                count++;
                *(sort_cur++) = n-cur;
            }        }
    }
}
    for(sort_cur = sort; *sort_cur; sort_cur++){
        printf("%d", *sort_cur);
    }
}

int main(){
    int i, n;
    int list[1000];
    char str[1000];
    char *tempstr;
    FILE * f = fopen("test.txt", "r");
    if(f == NULL){
        printf("file not found");
        return 1;
    }
    while(fgets(str, 1000, f)){
        n = 0;
        memset(list, 0, 1000);
        tempstr = strtok(str, " ");
        
        while(tempstr){
            list[n++] = atoi(tempstr);
            tempstr = strtok(NULL, " ");
        }
        sort(list, n);
        printf("0\n");
    }
}
