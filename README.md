# spandigital-coding-challenge
This is a simple application that calculates the ranking table for a league.

**Sample input:**
```
Lions 3, Snakes 3
Tarantulas 1, FC Awesome 0
Lions 1, FC Awesome 1
Tarantulas 3, Snakes 1
Lions 4, Grouches 0
```

**Expected output:**
```
1. Tarantulas, 6 pts
2. Lions, 5 pts
3. FC Awesome, 1 pt
3. Snakes, 1 pt
5. Grouches, 0 pts
```


## How to run the application
1. Install [Docker](https://www.docker.com/products/docker-desktop/).
2. Populate **sample-data.txt** file with your desired matches
3. Run ```docker-compose up league-app``` on your terminal
