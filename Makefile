# Must edit to fit your build folder
# Dependencies
# Required for modification to suit your operating system
# Where the .jar file live
# Typically opencv-x-x-x/build/bin/opencv-xxx.jar
JAR_DIR = /home/student/Desktop/opencv-4.6.0/build/bin/opencv-460.jar
# This is where your .so live
# Typically opencv-x-x-x/build/lib
LIB_DIR = /home/student/Desktop/opencv-4.6.0/build/lib

all: src
	make -C ./src JAR_DIR?=$(JAR_DIR) LIB_DIR=$(LIB_DIR)
run:
	make -C ./src JAR_DIR?=$(JAR_DIR) LIB_DIR=$(LIB_DIR) run
clean:
	make -C ./src clean
	make -C ./output clean
clean_media:
	make -C ./media clean