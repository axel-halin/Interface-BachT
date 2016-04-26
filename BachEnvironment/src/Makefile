#! /usr/bin/make -f 



# Pour produire un seul fichier a loader

complete : bacht_data.scala bacht_parser.scala bacht_store.scala bacht_simulator.scala
	cat bacht_data.scala bacht_parser.scala bacht_store.scala bacht_simulator.scala > bacht_cli.scala


# Pour compiler

sim : bacht_data.scala bacht_parser.scala bacht_store.scala bacht_simulator.scala
	scalac bacht_data.scala bacht_parser.scala bacht_store.scala bacht_simulator.scala


