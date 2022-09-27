all: src
	make -C ./src
run:
	make -C ./src run
clean:
	make -C ./src clean
