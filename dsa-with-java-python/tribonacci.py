class Tribonacci:
    def __init__(self):
        pass
    def tribonacci(self, n):
        a = 0
        b = 1
        c = 1
        for i in range(n):
            print(a)
            temp = a
            a = b
            b = c
            c = temp + a + b

t = Tribonacci()
t.tribonacci(10)