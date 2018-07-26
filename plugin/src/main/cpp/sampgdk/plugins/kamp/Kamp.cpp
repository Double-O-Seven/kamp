
#include "Kamp.hpp"

Kamp::Kamp() {

}

Kamp::~Kamp() {
	this->shutdown();
}

void Kamp::launch() {
	if (this->launched) {
		return;
	}


}

void Kamp::shutdown() {
	if (!this->launched) {
		return;
	}
}
