Stdlang docs contribution guide

## Working on chapters

Edit one of the files under `notebooks/stdlang_book` and use [Clay](https://scicloj.github.io/clay/) to render it as a notebook or to render specific forms.

See the [Clay setup](https://scicloj.github.io/clay/#setup) documentation for using Clay interactively ffrom your editor or IDE.

## Testing

The tests under `test/stdlang_book/` are generated automatically when rendering the chapters.

To add tests, see the [Clay test generation](https://scicloj.github.io/clay/#test-generation) documentation.

## Rendering the book

Run the code at `dev/render.clj`.

Caution: it overrides the whole `docs` directory and cleans up any previous files in it.

You will need the [Quarto](https://quarto.org/) CLI installed in your system.


