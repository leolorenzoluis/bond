
#pragma once

#include <bond/core/bond_version.h>

#if BOND_VERSION < 0x0422
#error This file was generated by a newer version of Bond compiler
#error and is incompatible with your version Bond library.
#endif

#if BOND_MIN_CODEGEN_VERSION > 0x0500
#error This file was generated by an older version of Bond compiler
#error and is incompatible with your version Bond library.
#endif

#include <bond/core/config.h>
#include <bond/core/containers.h>



namespace tests
{
    
    template <typename T>
    struct Foo
    {
        std::vector<std::vector<T> > aa;
        
        Foo()
        {
        }

        
#ifndef BOND_NO_CXX11_DEFAULTED_FUNCTIONS
        // Compiler generated copy ctor OK
        Foo(const Foo& other) = default;
#endif
        
#if !defined(BOND_NO_CXX11_DEFAULTED_MOVE_CTOR)
        Foo(Foo&& other) = default;
#elif !defined(BOND_NO_CXX11_RVALUE_REFERENCES)
        Foo(Foo&& other)
          : aa(std::move(other.aa))
        {
        }
#endif
        
        
#ifndef BOND_NO_CXX11_DEFAULTED_FUNCTIONS
        // Compiler generated operator= OK
        Foo& operator=(const Foo& other) = default;
#endif

        bool operator==(const Foo& other) const
        {
            return true
                && (aa == other.aa);
        }

        bool operator!=(const Foo& other) const
        {
            return !(*this == other);
        }

        void swap(Foo& other)
        {
            using std::swap;
            swap(aa, other.aa);
        }

        struct Schema;

    protected:
        void InitMetadata(const char*, const char*)
        {
        }
    };

    template <typename T>
    inline void swap(Foo<T>& left, Foo<T>& right)
    {
        left.swap(right);
    }
} // namespace tests

