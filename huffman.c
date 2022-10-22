// HUFFMAN CODE encode & decode
// Team C

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#define INPUT_MAX_LENGTH 100 
#define NO_CHARACTER     -1 

typedef struct NODE NODE;
struct NODE {
	bool   disposed;     // indication of node being empty
	char   character;    // NULL if not leaf node
	int    frequency;  
	NODE * parent_right; // has parent address if the node is right child of parent.
	NODE * parent_left;  // same, but left child of parent. More than one of thess have to be null.
};

NODE * make_node(char character, int frequency) {
	NODE * newnode = (NODE *) malloc(sizeof(NODE));
	newnode->character = character;
	newnode->frequency = frequency;
	newnode->disposed = false;
	return newnode;
}

int main(void) {

	int     i,j;                                   // for loop counters
	char    input_string[INPUT_MAX_LENGTH] = {0};  // input string
	int     input_length;                          // input string length 
	int     alphabet_count = 0;                    // count of each different chars in input
	NODE *  huffnode_list[129] = {0};              // list of nodes
	NODE *  huffnode_character_list[128] = {0};    // list of character nodes
	NODE ** cur;                                   // node list traversal cursor
	NODE ** cur_char;                              // character node traversal cursor

	// 0. Get string to encode
	printf("Enter string to encode: ");
	scanf("%s", input_string);
	input_length = strlen(input_string);

	// 1. Save each character and its frequency as single nodes
	cur_char = huffnode_character_list;
	for (i=0; i<input_length; i++) {
		bool found_same_character = false;
		for (cur = huffnode_list; *cur; cur++) {
			if ((*cur)->character == input_string[i]) {
				found_same_character = true;
				(*cur)->frequency++;
				break;
			}
		}
		if (!found_same_character) {
			*cur = make_node(input_string[i], 1);			
			*(cur_char++) = *cur;
			alphabet_count++;
		}
	}

	// 2. Do probing
	NODE * max_freq_dummy_node = make_node(NO_CHARACTER, INPUT_MAX_LENGTH);
	NODE ** small1;
	NODE ** small2;
	NODE * newnode;
	
	do {
		// break if node count < 2
		if (alphabet_count < 2)
			break;

		// find smallest two nodes
		small1 = &max_freq_dummy_node;
		small2 = small1;

		for (cur = huffnode_list; *cur; cur++) {
			if ((*cur)->disposed)
				continue;
			if ((*cur)->frequency < (*small1)->frequency) {
				small2 = small1;
				small1 = cur;
			} else if ((*cur)->frequency < (*small2)->frequency) {
				small2 = cur;
			}
		}

		// merge two nodes [character.]  [..............frequency.............]
		newnode = make_node(NO_CHARACTER, (*small1)->frequency + (*small2)->frequency);
		(*small1)->parent_left = newnode;
		(*small2)->parent_right = newnode;

		*small1 = newnode;
		(*small2)->disposed = true;

	// escape if sum of two frequencies are equal to input length
	} while (newnode->frequency != input_length);
		
	// 3. Print out results by traversing from each character nodes to top node
	char encoded[128][INPUT_MAX_LENGTH] = {0}; // Use ASCII code as index
	char record[INPUT_MAX_LENGTH];
	char record_rev[INPUT_MAX_LENGTH+1];
	char * record_cur;
	char * record_rev_cur;
	int trail_count;

	for (cur_char = huffnode_character_list; *cur_char; cur_char++, i++) {
		NODE * parent = *cur_char;
		memset(record, 0, INPUT_MAX_LENGTH);
		memset(record_rev, 0, INPUT_MAX_LENGTH);
		record_cur = record;
		record_rev_cur = record_rev;
		trail_count = 0;

		// traverse from character node to top node
		while (parent) {
			if (parent->parent_right) {
				*record_cur = '1';
				parent = parent->parent_right;
			} else if (parent->parent_left) {
				*record_cur = '0';
				parent = parent->parent_left;
			} else break;

			record_cur++;
			trail_count++;
		}
		record_cur--;

		// reverse the trail record
		while (trail_count-- > 0) {
			*(record_rev_cur++) = *(record_cur--);
		}

		// print out codes of each characters
		printf("%c: %s\n", (*cur_char)->character, record_rev);

		// save result to table
		strcpy(encoded[(*cur_char)->character], record_rev);
	}

	// 4. Encode string with calculated result, get encoded string size and compare with original
	int original_bits = input_length*8;
	int encoded_bits = 0;

	printf("encoded string: ");
	for (i=0; i<input_length; i++) {
		encoded_bits += strlen(encoded[input_string[i]]);
		printf("%s ", encoded[input_string[i]]);
	}

	printf("\nencoded string size: %d bits", encoded_bits);
	printf("\noriginal string size: %d bits", original_bits);
}
