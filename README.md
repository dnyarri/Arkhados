# Arkhados #

Arkhados is a fast paced multiplayer action PvP arena game where players
move around and try to kill each other by casting various spells.
Arkhados is entirely skill based, meaning that there is no automatic
targeting and no random elements like critical strike chance or dodge
chance.

Arkhados is very fast paced. There are no mana costs which makes
gameplay very fast. Games usually last less than 10 minutes.

Arkhados currently has two game modes: deathmatch and team deathmatch.

It's programmed in Java 8 using jMonkeyEngine 3.1.

<iframe src="https://player.vimeo.com/video/131966456" width="500" height="313" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe> <p><a href="https://vimeo.com/131966456">Arkhados 0.5 trailer</a> from <a href="https://vimeo.com/user41505101">William Linna</a> on <a href="https://vimeo.com">Vimeo</a>.</p>

## Dependencies ##

You need jMonkeyEngine 3.1-beta1-b002-SNAPSHOT.

## ToDos and contribution ideas ##

If you want to contribute, great! Here's  a big list of ideas I'd love to
see in Arkhados. Some of them are something that I'm going to
implement myself anyway, but I can't do all of them since I have to
focus on core features.

I will add more to this list and give more details on unexplained
items.

The list is unordered.

### Art 'n' assets ###

If you want to contribute or suggest art, remember that only
free/libre art is accepted, such as Creative Commons Attributions (CC-BY)
and Creative Commons Public Domain (CC0). This means that assets
that prohibit commercial use or derivation will not be accepted in Arkhados.

**Replace one of the character models with a better model** Currently
I'm using very low poly models for characters for various reasons. For
mage I couldn't find more detailed 3D model with armature animations ready.

I animated Venator myself because there was no other choice, but the
quality of my animations is pretty bad. It would be nice if they were
replaced.

**Find or create good music** In Arkhados every hero has a different
  set of music.

You can find good music with good licenses from opengameart.org.
And remember to use ogg-format because mp3 is patent
encumbered and not supported

**Create nice portraits for heroes** It would be nice to select hero
by clicking on a nice portrait instead of a button with text like
"Embermage" or "Venator". Portraits could be used on user guides too.


### Code ###

**Improve fog of war** Arkhados has fog of war but it should be
  improved visually and perhaps optimized. One way to optimize it
  would be to make framebuffer smaller but so far I've failed to do
  that. (I don't know if it's possible)

**Add nice trail effects**
Shotgun bullets would look lot nicer if they had some
kind of trails. Another nice use for trails would be to show nice red
trails when Venator swipes. I will add Pudge-like hook spell later so
trail system could be useful for that too. Maybe.

**In-game menu**
Allow players to access a menu during game so that
the player can configure keys, graphic settings etc. and immediately see
how it affects.

**Refactor code**

**Fix bugs**

**Dummy players**
It would make testing much faster if there was a nice way to add dummy
characters.

**Artificial Intelligence**

**Contribute on [ArkhadosNet](github.com/TripleSnail/ArkhadosNet)**
Arkhados uses ArkhadosNet for networking. I'm sure it could be
improved, and having some kind of tests for it would be very good.

### Design ###

**Design a new map**

**Design a new hero** The more there are heroes, the better (to certain
limit of course). If you have good idea, you can design a character and
give me a link. Just remember that I can't do everything. For example,
if there isn't decent model with rig and preferably working
animations, I wont add it, unless someone makes good enough model and
animations for it.

And the hero must be compatible with Arkhados' philosophy too, so there
can't be any random elements like dodge chance and no instant hit
spells.

**Create a custom UI style for Arkhados**

## License ##

Arkhados is mostly GPLv3 licensed. All source files contain license
header.
