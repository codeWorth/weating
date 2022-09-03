package com.andrew.weating.entries;

import lombok.Value;

import java.util.Collection;

@Value
public class FullEntry {
    Entry entry;
    Collection<Comment> comments;
}
