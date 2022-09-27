CLASS = A.class
OPENCV_PATH = /home/student/Desktop/opencv-4.6.0
DEPS = .:$(OPENCV_PATH)/build/bin/opencv-460.jar

all: $(CLASS)

%.class: %.java
	javac -cp $(DEPS) $<

clean:
	rm *.class
