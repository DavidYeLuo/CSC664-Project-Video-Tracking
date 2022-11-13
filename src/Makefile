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
	javac -cp $(DEPS) $<

%.class: %.java
	javac -cp $(DEPS) $<

run: dep $(CLASS)
	java -cp $(DEPS) Application

clean:
	rm *.class
	make -C $(IMAGE_READER_DIR) clean
	make -C $(CUSTOM_PROCESSING_DIR) clean

dep:
	make -C $(IMAGE_READER_DIR)
	make -C $(CUSTOM_PROCESSING_DIR)