class Tetranacci:
    def __init__(self,n):
        self.n = n
    def tetranacci(self):
        a, b, c, d = 0, 1, 1, 2
        for i in range(self.n):
            print(a)
            temp = a
            a = b
            b = c
            c = d
            d = temp + a + b + c

t = Tetranacci(10)
t.tetranacci()