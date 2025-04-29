/* test1.cm */
/* Tests basic token types */

void main(void) {
    int x;
    int y;

    /* Test numbers and operators */
    x = 42;
    y = 123;
    x = x + y;
    x = x - y;
    x = x * y;
    x = x / y;

    /* Test comparisons */
    if (x < y) {
        x = 0;
    }
    if (x <= y) {
        y = 0;
    }
    if (x > y) {
        x = 1;
    }
    if (x >= y) {
        y = 1;
    }
    if (x == y) {
        x = 2;
    }
    if (x != y) {
        y = 2;
    }

    /* Test while loop */
    while (x > 0) {
        x = x - 1;
    }

    return;
}