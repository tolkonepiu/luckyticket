.PHONY: clean

build:
	$(MAKE) -C go_random

clean:
	$(MAKE) -C go_random clean
