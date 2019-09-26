if [ -f target/player.java ]
then
    rm target/player.java
fi
for f in ./src/main/java/com/player/*.java; do tail -n +9 "$f" >> target/player.java; done