// main.c
#include <stdio.h>
#include <stdlib.h>

/* Forward declare the struct and typedef so you can write 'Node *' everywhere. */
typedef struct Node Node;

struct Node {
    int   val;
    Node *prev;
    Node *next;
};


void print_list(Node *head) {
    Node *curr = head;
    while (curr) {
        printf("%d", curr->val);
        if (curr->next) printf(" ");
        curr = curr->next;
    }
    printf("\n");
}

/* Insert a new node *after* the dummy (front insert). */
void add(Node *dummy, int val) {
    Node *newnode = (Node *)malloc(sizeof(Node));  // sizeof(Node), not sizeof(Node*)
    if (!newnode) {
        perror("malloc");
        exit(EXIT_FAILURE);
    }
    newnode->val  = val;
    newnode->prev = dummy;
    newnode->next = dummy->next;

    if (dummy->next) {
        dummy->next->prev = newnode;  // only if list was non-empty
    }
    dummy->next = newnode;
}

int find(Node *head,int val){
        Node *curr = head;
        while(curr){
                if(curr->val == val){
                        return 1;
                }
                curr = curr->next;
        }
        return 0;
}

void remove_node(Node *head, int val){
        Node *curr = head;
        while(curr){
                if(curr->val == val){
                        Node *prev = curr->prev;
                        Node *nxt = curr->next;
                        
                        prev->next = nxt;
                        if(nxt){
                                nxt->prev = prev;
                        }


                        curr->next = NULL;
                        curr->prev = NULL;
                }
                curr = curr->next;
        }
}


int main(void) {
    /* Create a sentinel (dummy) node that does not carry data. */
    Node *dummy = (Node *)malloc(sizeof(Node));
    dummy->val  = 0;      // unused
    dummy->prev = NULL;   // sentinel has no prev
    dummy->next = NULL;   // empty list initially

    print_list(dummy->next);          // prints nothing (empty line)

    for (int i = 1; i <= 5; i++) {
        add(dummy, i);                // inserts 1,2,3,4,5 at front (list becomes 5 4 3 2 1)
    }
    print_list(dummy->next);

    printf(find(dummy->next,3)?"Found value 3":"Didn't find 3");
    printf("\n");
    printf(find(dummy->next,5)?"Found value 5":"Didn't find 5");
    printf("\n");
    printf(find(dummy->next,100)?"Found value 100":"Didn't find 100");
    printf("\n");

    remove_node(dummy,5);
    remove_node(dummy,1);
    remove_node(dummy,100);
    print_list(dummy->next);

    return 0;
}
