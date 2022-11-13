all: src
	make -C ./src
run:
	make -C ./src run
clean:
	make -C ./src clean
	make -C ./output clean
clean_media:
	make -C ./media clean