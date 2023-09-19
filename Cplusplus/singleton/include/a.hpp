#include <singleton.hpp>

class A : public util::singleton<A>
{
public:
    int a;
};