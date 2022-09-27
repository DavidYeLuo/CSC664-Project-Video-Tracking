CLASS = ImageReader.class
DEPS = .:temp
DEPS_DIR = ./temp

all: dep $(CLASS)

%.class: %.java
	javac -cp $(DEPS) $<

clean:
	rm *.class
	make -C $(DEPS_DIR) clean

dep:
	make -C $(DEPS_DIR)
