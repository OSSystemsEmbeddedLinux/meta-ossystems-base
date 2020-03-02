ASCIIDOCTOR = asciidoctor

HTML = ossystems-factory-defaults.html

all: $(HTML)

%.html: %.adoc
	$(ASCIIDOCTOR) -b html5 $<

clean:
	rm -rf *~ $(HTML)

.PHONY: clean all install
