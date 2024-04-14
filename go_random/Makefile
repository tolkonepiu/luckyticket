UNAME := $(shell uname)
UNAME_M := $(shell uname -m)
BINARIES_PATH := binaries/

ifeq ($(UNAME), Darwin)
    DYLIB_EXT := .dylib
    FLAGS := -ldflags -s
    PATH_PREFIX := osx_
else ifeq ($(UNAME), Linux)
    DYLIB_EXT := .so
    FLAGS := ''
    PATH_PREFIX := linux_
else
    $(error Unknown system: $(UNAME))
endif

ifeq ($(UNAME_M), arm64)
    ARCH := arm64
else ifeq ($(UNAME_M), x86_64)
    ARCH := 64
else
    $(error Unknown arch: $(UNAME_M))
endif

librandom$(DYLIB_EXT):
	go build $(FLAGS) -o $(BINARIES_PATH)/$(PATH_PREFIX)$(ARCH)/$@ -buildmode=c-shared random.go

clean:
	rm -rf $(BINARIES_PATH)
