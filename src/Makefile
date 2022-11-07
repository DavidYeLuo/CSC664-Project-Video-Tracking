MAIN = Application.java
CLASS = Application.class
DEPS = .

# DEPS DIR
DEPS_DIR = ImageReader CustomProcessing
IMAGE_READER_DIR = ImageReader
CUSTOM_PROCESSING_DIR = CustomProcessing

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
	#make -C $(DEPS_DIR) clean
	make -C $(IMAGE_READER_DIR) clean
	make -C $(CUSTOM_PROCESSING_DIR) clean

dep:
#	make -C $(DEPS_DIR) JAR_DIR?=$(JAR_DIR) LIB_DIR=$(LIB_DIR)
	make -C $(IMAGE_READER_DIR) JAR_DIR?=$(JAR_DIR) LIB_DIR=$(LIB_DIR)
	make -C $(CUSTOM_PROCESSING_DIR) JAR_DIR?=$(JAR_DIR) LIB_DIR=$(LIB_DIR)