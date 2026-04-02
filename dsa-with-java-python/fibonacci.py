class Fibonacci:
    def __init__(self):
        pass
    def fib(self, n):
        a, b = 0, 1
        for i in range(n):
            print(a)
            temp = a
            a = b
            b = temp + b
            


f = Fibonacci()
f.fib(10)
    

