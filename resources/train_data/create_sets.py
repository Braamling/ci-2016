import random
import csv

def main():
    train_size = .70
    test_size = .15
    valid_size = .15

    # Load and combine all csv files
    with open("aalborg.csv") as f:
        r = csv.reader(f)
        header, l = next(r), list(r)

    with open("alpine-1.csv") as f:
        r = csv.reader(f)
        _, l_1 = next(r), list(r)

    with open("f-speedway.csv") as f:
        r = csv.reader(f)
        _, l_2 = next(r), list(r)

    combined = l + l_1 + l_2

    random.shuffle(combined)

    n_items = len(combined)

    start = 0
    train_end = int(n_items * train_size)
    test_end = int(train_end +  n_items * test_size)
    valid_end = n_items - 1
    
    # write a train, test and valid set
    with open("train.csv", "wb") as f:
        csv.writer(f).writerows([header] + combined[0: train_end])

    with open("test.csv", "wb") as f:
        csv.writer(f).writerows([header] + combined[train_end: test_end])

    with open("valid.csv", "wb") as f:
        csv.writer(f).writerows([header] + combined[test_end: valid_end])


if __name__ == '__main__':
    main()