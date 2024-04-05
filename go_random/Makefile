UNAME := $(shell uname)

ifeq ($(UNAME), Darwin)
    DYLIB_EXT := .dylib
    FLAGS := -ldflags -s
else
    DYLIB_EXT := .so
    FLAGS := ''
endif

random$(DYLIB_EXT):
	env GOOS=darwin GOARCH=arm64 go build $(FLAGS) -o ../src/main/resources/natives/osx_arm64/librandom.dylib -buildmode=c-shared random.go

	# TODO multiarch
	# env GOOS=linux GOARCH=amd64 go build -o ../src/main/resources/natives/linux_64/librandom.so -buildmode=c-shared random.go

clean:
	rm -rf ../src/main/resources/natives/
