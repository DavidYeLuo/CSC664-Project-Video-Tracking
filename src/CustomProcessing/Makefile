CLASS = FrameAvg.class
DEPS = .

all: $(CLASS)

%.class: %.java
	javac -cp $(DEPS) $<

clean:
	rm *.class
	#make -C $(DEPS_DIR) clean

dep:
	make -C $(DEPS_DIR)