MAIN = Application.java
CLASS = Application.class
DEPS = .
DEPS_DIR = ImageReader

.PHONY: all run clean dep

all: dep $(CLASS)

Application.class: Application.java
	javac -cp $(DEPS):$(JAR_DIR) $<

%.class: %.java
	javac -cp $(DEPS) $<

run: dep $(CLASS)
	java -cp $(DEPS):$(JAR_DIR) \
		-Djava.library.path=$(LIB_DIR) \
		Application

clean:
	rm *.class
	make -C $(DEPS_DIR) clean

dep:
	make -C $(DEPS_DIR) JAR_DIR?=$(JAR_DIR) LIB_DIR=$(LIB_DIR)
