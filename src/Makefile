MAIN = Application.java
CLASS = Application.class
DEPS = .:ImageReader
DEPS_DIR = ./ImageReader

# Dependencies
# Required for modification to suit your operating system
# Where the .jar file live
# Typically opencv-x-x-x/build/bin/opencv-xxx.jar
JAR_DIR = /home/student/Desktop/opencv-4.6.0/build/bin/opencv-460.jar
# This is where your .so live
# Typically opencv-x-x-x/build/lib
LIB_DIR = /home/student/Desktop/opencv-4.6.0/build/lib

.PHONY: all run clean dep

all: dep $(CLASS)

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
	make -C $(DEPS_DIR)
