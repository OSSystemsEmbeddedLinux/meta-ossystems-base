require ../rust/rust-source.inc
require ../rust/rust-snapshot.inc
require cargo.inc
BBCLASSEXTEND = "native nativesdk"
RUSTLIB_DEP:class-nativesdk = ""