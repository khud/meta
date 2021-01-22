package metacourse.slides

import io.kotex.bibtex.*

object Bib {
    val sorensenMortenGluck96 = article(
        id = "SorensenMortenGluck96",
        author = "Sørensen, Morten and Glück, R. and Jones, Neil",
        year = 1996,
        month = "11",
        pages = "811-838",
        title = "A positive supercompiler",
        volume = 6,
        journal = "Journal of Functional Programming",
        doi = "10.1017/S0956796800002008"
    )

    val mcCarty60 = article(
        id = "McCarty60",
        author = "McCarthy, John",
        title = "Recursive Functions of Symbolic Expressions and Their Computation by Machine, Part I",
        year = 1960,
//    issue_date = {April 1960},
//    publisher = "Association for Computing Machinery",
//    address = {New York, NY, USA},
        volume = 3,
        number = 4,
//    issn = {0001-0782},
//    url = https://doi.org/10.1145/367177.367199},
        doi = "10.1145/367177.367199",
        journal = "Commun. ACM",
        month = "apr",
        pages = "184–195"
//    numpages = {12}
    )

    val turchin88algorithm = article(
        id = "Turchin1988algorithm",
        title = "The algorithm of generalization in the supercompiler",
        author = "Turchin, Valentin F",
        journal = "Partial Evaluation and Mixed Computation",
        volume = 531,
        pages = "549",
        year = 1988
//    publisher={North-Holland Amsterdam}
    )

    val gluck1993occam = inProceedings(
        id = "Gluck1993occam",
        title = "Occam's razor in metacomputation: the notion of a perfect process tree",
        author = "Gl{\"u}ck, Robert and Klimov, Andrei V",
        bookTitle = "International Workshop on Static Analysis",
        pages = "112--123",
        year = 1993,
        organization = "Springer"
    )


    /*
@inproceedings{Truffle17,
    author = {W\"{u}rthinger, Thomas and Wimmer, Christian and Humer, Christian and W\"{o}\ss, Andreas and Stadler, Lukas and Seaton, Chris and Duboscq, Gilles and Simon, Doug and Grimmer, Matthias},
    title = {Practical Partial Evaluation for High-performance Dynamic Language Runtimes},
    booktitle = {Proceedings of the 38th ACM SIGPLAN Conference on Programming Language Design and Implementation},
    series = {PLDI 2017},
    year = {2017},
    isbn = {978-1-4503-4988-8},
    location = {Barcelona, Spain},
    pages = {662--676},
    numpages = {15},
    url = {http://doi.acm.org/10.1145/3062341.3062381},
    doi = {10.1145/3062341.3062381},
    acmid = {3062381},
    publisher = {ACM},
    address = {New York, NY, USA},
    keywords = {dynamic languages, language implementation, optimization, partial evaluation, virtual machine}
}
*/
    val truffle17 = inProceedings(
        "Truffle17",
        author = "W\"{u}rthinger, Thomas and Wimmer, Christian and Humer, Christian and W\"{o}ss, Andreas and Stadler, Lukas and Seaton, Chris and Duboscq, Gilles and Simon, Doug and Grimmer, Matthias",
        title = "Practical Partial Evaluation for High-performance Dynamic Language Runtimes",
        bookTitle = "Proceedings of the 38th ACM SIGPLAN Conference on Programming Language Design and Implementation",
        series = "PLDI 2017",
        year = 2017,
        pages = "662--676",
        publisher = "ACM",
        address = "New York, NY, USA"
    )

    /*
@book{AbramovMeta,
    author = "Абрамов С. М.",
    title = "Методы метавычислений и их применение",
    publisher = "Издательство «Университет города Переславля имени А.К.Айламазяна»",
    year = "2006"
}
*/
    val abramovMeta = book(
        "AbramovMeta",
        author = "Абрамов С. М.",
        title = "Методы метавычислений и их применение",
        publisher = "Издательство «Университет города Переславля имени А.К.Айламазяна»",
        year = 2006
    )

    /*
@book{AbramovMetaDop,
    author = "Парменова Л. В., Абрамов С. М.",
    title = "Метавычисления и их применение. Суперкомпиляция",
    publisher = "Издательство «Университет города Переславля имени А.К.Айламазяна»",
    year = "2006"
}
*/
    val abramovMetaDop = book(
        "abramovMetaDop",
        author = "Парменова Л. В., Абрамов С. М.",
        title = "Метавычисления и их применение. Суперкомпиляция",
        publisher = "Издательство «Университет города Переславля имени А.К.Айламазяна»",
        year = 2006
    )

    /*
@article{Futamura71,
    author = "Y. Futamura",
    title = "Partial evaluation of computation process–an approach to a compiler-compiler",
    journal = "Systems, Computers, Controls",
    volume = "2",
    number = "5",
    pages = "721–-728",
    year = "1971"
}
*/
    val futamura71 = article(
        "Futamura71",
        author = "Y. Futamura",
        title = "Partial evaluation of computation process–an approach to a compiler-compiler",
        journal = "Systems, Computers, Controls",
        volume = 2,
        number = 5,
        pages = "721–-728",
        year = 1971
    )

    /*
@book{Jones93,
    author = "C.K. Gomard, N.D. Jones and P. Sestoft",
    title = "Partial Evaluation and Automatic Program Generation",
    publisher = "Prentice Hall International",
    year = "1993"
}
*/
    val jones93 = book(
        "Jones93",
        author = "C.K. Gomard, N.D. Jones and P. Sestoft",
        title = "Partial Evaluation and Automatic Program Generation",
        publisher = "Prentice Hall International",
        year = 1993
    )
}
