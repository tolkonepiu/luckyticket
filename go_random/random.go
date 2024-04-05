package main

import (
    "math/rand"
    "C"
)

//export Random
func Random(n int, seed int64) int {
	source := rand.NewSource(seed)
    random := rand.New(source)

	return random.Intn(n)
}

func main() {} // Required but ignored
