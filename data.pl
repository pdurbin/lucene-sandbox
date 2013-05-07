#!/usr/bin/perl
use strict;
use warnings;
use v5.10;
my @colors = qw{blue green yellow};
my @birds  = qw{sparrow finch wren};
my @trees  = qw{spruce oak maple};
#my @places = qw{Boston Madison Dayton};

my $count;
for my $color (@colors) {
    for my $bird (@birds) {
        for my $tree (@trees) {
#            for my $place (@places) {
#                say "a $color $bird on a $tree in $place";
#            }
                say "\"a $color $bird on a $tree\",";
                $count++;
        }
    }
}
for my $count (1 .. scalar $count){
    say "\"doc$count\",";
}
