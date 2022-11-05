CLASS =
DEPS = .
DEPS_DIR = ./temp

all: dep $(CLASS)

%.class: %.java
	javac -cp $(DEPS):$(JAR_DIR) $<

clean:
	rm *.class
	make -C $(DEPS_DIR) clean

dep:
	make -C $(DEPS_DIR)
