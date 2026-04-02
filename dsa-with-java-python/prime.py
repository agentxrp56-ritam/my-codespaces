class Prime:
    def prime(self):
        for i in range(1,101):
            count=0
            for j in range(1,i+1):
                if i%j==0:
                    count+=1
            if count==2:
                print(i,"is a prime number")
            else:
                print(i,"is not a prime number")


p=Prime()
p.prime()