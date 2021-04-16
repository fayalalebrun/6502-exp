basic.o: clean
	ca65 --cpu 65c02 -g -l min_mon.lst --feature labels_without_colons -o basic.o min_mon.asm

basic.bin: clean basic.o
	ld65  -vm -C basic.cfg -m basic.map -o basic.bin basic.o

.PHONY: burn_basic
burn_basic: basic.bin
	minipro -p AT28C256 -w basic.bin

.PHONY: as
as:
	acme main.acme

.PHONY: burn
burn: as
	minipro -p AT28C256 -w main.o

.PHONY: burn_test
burn_test:
	acme test.acme
	minipro -p AT28C256 -w main.o

.PHONY: clean
clean:
	$(RM) *.o *.lst *.map *.bin *.run *.mon *.c1p
