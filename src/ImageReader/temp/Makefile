CLASS = A.class
DEPS =

all: $(CLASS)

%.class: %.java
	javac -cp $(DEPS):$(JAR_DIR) $<

clean:
	rm *.class
