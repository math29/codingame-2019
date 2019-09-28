if [ -f target/player.java ]
then
    rm target/player.java
fi

# Add all classes to file + remove all imports + remove public keyword from classes
for f in ./src/main/java/com/player/*/**; do tail -n +1 "$f" >> target/player.java; done
sed -i.bak "s/public class/class/;s/public abstract class/abstract class/;/^import/d" target/player.java

# Add player class content at the beginning of the file
cat ./src/main/java/com/player/Player.java | cat - target/player.java > temp && mv temp target/player.java

# Remove packages from files + com imports
sed -i.bak "/package com./d;/^import com./d" target/player.java