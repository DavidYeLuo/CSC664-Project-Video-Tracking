MAIN = Application.java
CLASS = Application.class
DEPS = .:ImageReader
DEPS_DIR = ./ImageReader

.PHONY: all run clean dep

all: dep $(CLASS)

%.class: %.java
	javac -cp $(DEPS) $<

run: $(CLASS)
	java -cp $(DEPS) Application

clean:
	rm *.class
	make -C $(DEPS_DIR) clean

dep:
	make -C $(DEPS_DIR)
