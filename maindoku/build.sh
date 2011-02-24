pdflatex main.tex
makeindex document.glo -s document.ist -t document.glg -o document.gls
pdflatex main.tex
rm *.log
rm *.aux
rm *.out
rm *.toc
xpdf main.pdf
