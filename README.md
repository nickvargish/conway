# conway

Conway's Game of Life implemented in Clojure.

## Compilation

Download from https://gitlab.bandersnatch.org/nav/conway.

    shell$ lein bin

Binary is `target/bin/conway`

## Usage

In the REPL:

    (load-file "src/conway/core.clj")
    (ns conway.core)
    (run-file "data/rpentomino.life" 10)

On the command-line:

    shell$ target/bin/conway data/rpentomino.life 10

## Options

No options.

### Bugs and Caveats

Only handles Life 1.05 files properly, though there's code for 1.06.

No doubt many bugs.

## License

Copyright © 2015 Nick Vargish

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
