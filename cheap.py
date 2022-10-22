m,n = list(map(int, input().split()))
path = [list(map(int, input().split())) for _ in range(m)]


def dothing(row, col, record, sum):
    record.append(path[row][col])
    sum += path[row][col]
    if col == n-1:
        return (record, sum)
    else:
        total_sb = dothing((row-1)%m, col+1, list(record), sum)
        total_eq = dothing((row)%m,   col+1, list(record), sum)
        total_ad = dothing((row+1)%m, col+1, list(record), sum)

        mintot = total_sb
        if total_eq[1] < mintot[1]: mintot = total_eq
        if total_ad[1] < mintot[1]: mintot = total_ad
        return mintot

print(dothing(0, 0, [], 0))